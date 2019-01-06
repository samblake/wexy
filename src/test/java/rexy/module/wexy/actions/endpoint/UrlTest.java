package rexy.module.wexy.actions.endpoint;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UrlTest {
	
	@Test
	public void fromUrl() {
		Url url = Url.fromUrl("/test/{123}?abc={abc}&xyz={xyz}");
		assertThat(url.getParameters()).hasSize(3);
		assertThat(url.getParameters().get(0)).isEqualTo("123");
		assertThat(url.getParameters().get(1)).isEqualTo("abc");
		assertThat(url.getParameters().get(2)).isEqualTo("xyz");
	}
	
}