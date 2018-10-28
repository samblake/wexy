package rexy.module.wexy.mbean;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class QueryBuilder extends MBeanQueryBuilder {
	
	private final String base;
	
	public QueryBuilder() {
		this("Rexy");
	}
	
	public QueryBuilder(String base) {
		this.base = base;
	}
	
	public TypeQueryBuilder withType(String type) {
		return new TypeQueryBuilder(type);
	}
	
	@Override
	protected StringBuilder getName() {
		return new StringBuilder(base).append(':');
	}
	
	public class TypeQueryBuilder extends MBeanQueryBuilder {
		private final String type;
		
		public TypeQueryBuilder(String type) {
			this.type = type;
		}
		
		public ScopeQueryBuilder withScope(String scope) {
			return new ScopeQueryBuilder(scope);
		}
		
		@Override
		protected StringBuilder getName() {
			return QueryBuilder.this.getName().append("type=").append(type).append(',');
		}
		
		public class ScopeQueryBuilder extends MBeanQueryBuilder {
			private final String scope;
			
			public ScopeQueryBuilder(String scope) {
				this.scope = scope;
			}
			
			public ComponentQueryBuilder withComponent(String component) {
				return new ComponentQueryBuilder(component);
			}
			
			public NameQueryBuilder withName(String name) {
				return new NameQueryBuilder(this, name);
			}
			
			@Override
			protected StringBuilder getName() {
				return TypeQueryBuilder.this.getName().append("scope=").append(scope).append(',');
			}
			
			public class ComponentQueryBuilder extends MBeanQueryBuilder {
				private final String component;
				
				public ComponentQueryBuilder(String component) {
					this.component = component;
				}
				
				@Override
				protected StringBuilder getName() {
					return ScopeQueryBuilder.this.getName().append("component=").append(scope).append(',');
				}
				
				public NameQueryBuilder withName(String name) {
					return new NameQueryBuilder(this, name);
				}
				
			}
			
		}
		
	}
	
	public class NameQueryBuilder extends MBeanQueryBuilder {
		private final MBeanQueryBuilder parent;
		private final String name;
		
		public NameQueryBuilder(MBeanQueryBuilder parent, String name) {
			this.parent = parent;
			this.name = name;
		}
		
		@Override
		protected StringBuilder getName() {
			return parent.getName().append("name=").append(name);
		}
		
		@Override
		protected ObjectName buildObjectName() throws MalformedObjectNameException {
			return new ObjectName(getName().toString());
		}
	}
	
}