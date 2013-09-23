package com.coreweb.extras.email;


import java.security.GeneralSecurityException;
import java.util.Properties;  

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;  
import javax.mail.MessagingException;  
import javax.mail.PasswordAuthentication;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.MimeMultipart;


  
  
public class EnviarCorreo{  
  
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";  
    private static final String SMTP_PORT = "587";  
    private static final String emailFromAddress = "yhaguyrepuestos@gmail.com";
    private static final String emailFromPasswrd = "yrmkt1970";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
   
    
    public void sendSSLMessage(String recipients[], String[] recipientsCC,String[] recipientsCCO, String subject,  
            String message, String from, String fileName, String path) throws Exception {  
        boolean debug = false;         
        Properties props = new Properties();  

   
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", SMTP_PORT);
  
        Session session = Session.getDefaultInstance(props,  
                new javax.mail.Authenticator() {  
                    protected PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(emailFromAddress,  
                                emailFromPasswrd);  
                    }  
                });  
  
        session.setDebug(debug);  

    	BodyPart mensaje = new MimeBodyPart();
    	mensaje.setText(message);
    	
    	MimeMultipart multiParte = new MimeMultipart();
    	multiParte.addBodyPart(mensaje);

        if (path != null){
            BodyPart adjunto = new MimeBodyPart();
        	adjunto.setDataHandler(new DataHandler(new FileDataSource(path)));
        	adjunto.setFileName(fileName);
        	multiParte.addBodyPart(adjunto);
        }
    	
  
        Message msg = new MimeMessage(session);
        //aqui cambiar la cuenta, para que reciba como parametro..provisoriamente usa esta cuenta
        InternetAddress addressFrom = new InternetAddress(emailFromAddress);  
        msg.setFrom(addressFrom);
          
        InternetAddress[] addressTo = new InternetAddress[recipients.length];  
        for (int i = 0; i < recipients.length; i++) {  
            addressTo[i] = new InternetAddress(recipients[i]);  
        }  
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        
        //verifica si destinatario CC no es vacio
        if (recipientsCC[0].isEmpty()==false) {
        	
        	InternetAddress[] addressToCC = new InternetAddress[recipientsCC.length];  
            for (int i = 0; i < recipientsCC.length; i++) {  
                addressToCC[i] = new InternetAddress(recipientsCC[i]);  
            }  
            msg.setRecipients(Message.RecipientType.CC, addressToCC);
            
		}
        
      //verifica si destinatario CCO no es vacio
        if (recipientsCCO[0].isEmpty()==false) {
        	
        	InternetAddress[] addressToCCO = new InternetAddress[recipientsCCO.length];  
            for (int i = 0; i < recipientsCCO.length; i++) {  
                addressToCCO[i] = new InternetAddress(recipientsCCO[i]);  
            }  
            msg.setRecipients(Message.RecipientType.BCC, addressToCCO);
            
		}
        
  
        //Configura el asunto y el tipo de contenido 
        msg.setSubject(subject);
        msg.setContent(multiParte);        
        Transport.send(msg);
        
    }
    
    
    public static void main(String[] args) {
		try {
			
			EnviarCorreo co = new EnviarCorreo();
			String[] recipients = {"daniel.omar.romero@gmail.com"};
			String[] recipientsCC = {""};
			String[] recipientsCCO = {""};
			String subject = "asunto";
			String message = "mensaje";
			String from = emailFromAddress;
			String fileName = null;
			String path = null;
			
			co.sendSSLMessage(recipients, recipientsCC, recipientsCCO, subject, message, from, fileName, path);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} 