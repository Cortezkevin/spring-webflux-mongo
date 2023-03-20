package com.kevin.webfluxmongo.service;

import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.exception.CustomException;
import com.kevin.webfluxmongo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    public Mono<String> sendHtmlTemplateEmail(String to ){
        Mono<User> user = userRepository.findByEmail( to );
        return user.flatMap( userFound -> Mono.fromCallable(() -> {
                    try {
                        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        Context context = new Context();

                        UUID token = UUID.randomUUID();
                        userFound.setTokenPassword( token.toString() );

                        userRepository.save( userFound ).subscribe();

                        Map<String, Object> model = new HashMap<>();
                        model.put("username", userFound.getUsername());
                        model.put("url", "http://localhost:4200/changePassword/" + token);
                        context.setVariables( model );

                        String htmlText =  templateEngine.process("email_template", context);
                        helper.setFrom("cortezkevinq@gmail.com");
                        helper.setTo(to);
                        helper.setSubject("Prueba envio email");
                        helper.setText(htmlText, true);

                        javaMailSender.send(mimeMessage);
                        return "Email de confirmacion enviado";
                    }catch (MessagingException e) {
                        return "Ocurrio un error al enviar el email";
                    }
                }) )
                .switchIfEmpty( Mono.error( new CustomException(HttpStatus.BAD_REQUEST, "Email not exists")) );
    }

}

