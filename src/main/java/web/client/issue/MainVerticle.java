package web.client.issue;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class MainVerticle extends AbstractVerticle{
	  
	@Override
	public void start(Promise<Void> promise) throws Exception{
		System.out.println("In start ");
		WebClientOptions webClientOptions = new WebClientOptions()
				.setFollowRedirects(true);
		
		WebClient client = WebClient.create(vertx, webClientOptions);
		String Url1 = "https://redirect.cfapps.eu10.hana.ondemand.com/api/redirect";
		HttpRequest<Buffer> request = client.requestAbs(HttpMethod.GET, Url1).timeout(800000);
		request.send(responseHandler->{
			if(responseHandler.succeeded()) {
				int code = responseHandler.result().statusCode();
				if(code == HttpResponseStatus.OK.code()) {
					System.out.println("Success response");
				} else if(code >= 300 && code < 400 ){
					System.out.println("Maximum redirects exceeded");
				} else {
					System.out.println("Other response code: " + code);
				}
			} else {
				System.out.println("Error occured reason: " + responseHandler.cause());
			}
			System.out.println("end closing vertx");
			vertx.close();
		});
	}
}
