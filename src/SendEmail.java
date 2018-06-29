// File Name SendEmail.java

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import au.com.bytecode.opencsv.CSVReader;


public class SendEmail {

   public static void main(String [] args) {    
      
	  HashMap<Person, Person> map = new HashMap<>();
	  
	  final String isaEmail = "temp@gmail.com";
	  final String pass = "password";
	  
	  String csvFile = "mainFile.csv";

      CSVReader reader = null;
      try {
          reader = new CSVReader(new FileReader(csvFile));
          String[] line;
          reader.readNext();
          while ((line = reader.readNext()) != null) {
              Person mentor = new Person();
              mentor.setName(line[0].trim()+" "+line[1].trim());
              mentor.setEmail(line[2].trim());
              
              Person mentee = new Person();
              mentee.setName(line[5].trim()+" "+line[6].trim());
              mentee.setEmail(line[7].trim());
              
              map.put(mentee, mentor);
              
              System.out.println(mentee.getName()+":"+mentor.getName());
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
     
      String host = "smtp.gmail.com";
      
      String port = "587";
      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      
      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", port);
      properties.put("mail.smtp.starttls.enable","true");
      properties.put("mail.smtp.auth", "true"); 
      

      // Get the default Session object.
      Authenticator auth = new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(isaEmail, pass);
          }
        };

      Session session = Session.getInstance(properties, auth);
      
      try {
         

         // Send message
         Transport t = session.getTransport();
         t.connect();
         try {
        	 int counter = 0;
           for(Person mentee: map.keySet()) {
        	  try {
        		  // Recepient's email ID i.e Mentee needs to be mentioned
           	   String to = mentee.getEmail();
           	   
           	// cc email ID i.e Mentor needs to be mentioned
           	   String mentor = map.get(mentee).getEmail();

           	   // Sender's email ID needs to be mentioned
           	   String from = isaEmail;

           	   // Create a default MimeMessage object.
   	           MimeMessage message = new MimeMessage(session);
   	
   	           // Set From: header field of the header.
   	           message.setFrom(new InternetAddress(from));
   	
   	           // Set To: header field of the header.
   	           message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
   	           
   	           // cc
   	           message.addRecipient(Message.RecipientType.CC, new InternetAddress(mentor));
   	           
   	           // Set Subject: header field
   	           message.setSubject("ISA Buddy Progam - Fall 2018");
   	           // Now set the actual message
   	           message.setContent("Hello "+mentee.getName()+",\r<br>" + 
   		           		"Hope you are doing well!\r<br>" + 
   		           		"<br>The Indian Students Association is glad to have you on board for the \"<strong>ISA Buddy Program - Fall 2018</strong>\". This program aims to connect incoming students with the current students and alumni of UT Dallas. You would be assisted for your queries with regards to application, course, documentation, travel, housing, etc.\r<br>" + 
   		           		"<br>We are happy to introduce <strong>"+map.get(mentee).getName()+"</strong> (copied in the mail) as your Mentor, who can be contacted for your queries. You can switch to other mediums like Facebook, Hangout, WhatsApp, etc. depending on the discussion with your mentor. Please email your mentor directly to move ahead with the communication. \r<br>" + 
   		           		"\r<br>" + 
   		           		"Kindly, understand that your mentor has volunteered for this opportunity, thus it’s important to respect the mentor's personal space.\r<br>" + 
   		           		"\r<br>" + 
   		           		"To make this initiative efficient, a team of ISA officers will be helping you. In case you don't receive any response within 48 hours of your email to your mentor, kindly write us at isabuddyprogram@gmail.com\r<br>" + 
   		           		"\r<br>" + 
   		           		"<strong>"+map.get(mentee).getName()+"</strong> - Thanks for volunteering for this program. We believe your experience at UT Dallas would be of great help for the incoming students. Please, feel free to contact us at isabuddyprogram@gmail.com, in case of any doubts.\r<br>" + 
   		           		"Wishing you all the very best!\r<br>" + 
   		           		"\r<br>" + 
   		           		"Regards,\r<br>" + 
   		           		"Career Team,\r<br>" + 
   		           		"Indian Students Association\r<br>" + 
   		           		"", "text/html; charset=utf-8");
   	           message.saveChanges();
   	           t.sendMessage(message, message.getAllRecipients());
   	           counter++;
   	           System.out.println("message-"+counter+" sent");
        	  } catch(Exception e) {
        		  System.out.println(e.getMessage());
        	  }
           }
         } finally {
           t.close();
         }
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
}
