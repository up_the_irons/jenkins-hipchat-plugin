package jenkins.plugins.hipchat;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class StandardHipChatService implements HipChatService {

   private static final Logger logger = Logger.getLogger(StandardHipChatService.class.getName());

   private String token;
   private String roomId;
   private String from;

   public StandardHipChatService(String token, String roomId, String from) {
      super();
      this.token = token;
      this.roomId = roomId;
      this.from = from;
   }

   public void publish(String message) {
      logger.info("Posting: " + from + " to " + roomId + ": " + message);

      HttpClient client = new HttpClient();
      String url = "https://api.hipchat.com/v1/rooms/message?auth_token=" + token;
      PostMethod post = new PostMethod(url);
      try {
         post.addParameter("from", from);
         post.addParameter("room_id", roomId);
         post.addParameter("message", message);
         client.executeMethod(post);
      }
      catch(HttpException e) {
         throw new RuntimeException("Error posting to HipChat", e);
      }
      catch(IOException e) {
         throw new RuntimeException("Error posting to HipChat", e);
      }
      finally {
         post.releaseConnection();
      }
   }

   public void rooms() {
      HttpClient client = new HttpClient();
      String url = "https://api.hipchat.com/v1/rooms/list?format=json&auth_token=" + token;
      GetMethod get = new GetMethod(url);
      try {
         client.executeMethod(get);
         logger.info(get.getResponseBodyAsString());
      }
      catch(HttpException e) {
         throw new RuntimeException("Error posting to HipChat", e);
      }
      catch(IOException e) {
         throw new RuntimeException("Error posting to HipChat", e);
      }
      finally {
         get.releaseConnection();
      }
   }

}
