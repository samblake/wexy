package rexy.module.wexy;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static rexy.module.wexy.Utils.until;

public class UtilsTest {
	
	@Test
	public void testUntil() {
		assertThat(until("abc", '?')).isEqualTo("abc");
		assertThat(until("abc?123", '?')).isEqualTo("abc");
		assertThat(until("", '?')).isEmpty();
		assertThat(until("?", '?')).isEmpty();
	}
	
}