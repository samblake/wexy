package rexy.module.wexy.actions.endpoint;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static rexy.http.Method.GET;

public class UriTest {
	
	@Test
	public void fromUrl() {
		Uri uri = Uri.fromUrl(GET,"/test/{123}?abc={abc}&xyz={xyz}");
		assertThat(uri.getMethod()).isEqualTo("GET");
		assertThat(uri.getParameters()).hasSize(3);
		assertThat(uri.getParameters().get(0)).isEqualTo("123");
		assertThat(uri.getParameters().get(1)).isEqualTo("abc");
		assertThat(uri.getParameters().get(2)).isEqualTo("xyz");
	}
	
}