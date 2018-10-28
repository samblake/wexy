package rexy.module.wexy.mbean;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class MBeanRepo {
	
	private final MBeanServer server;
	
	public MBeanRepo() {
		this.server = ManagementFactory.getPlatformMBeanServer();
	}
	
	public Set<ObjectInstance> search(MBeanQueryBuilder queryBuilder) {
		return server.queryMBeans(queryBuilder.build(), null);
	}
	
}