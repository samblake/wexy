package rexy.module.wexy.builders;

import rexy.config.model.Api;
import rexy.config.model.Endpoint;
import rexy.module.wexy.model.Breadcrumbs;
import rexy.module.wexy.model.Breadcrumbs.Breadcrumb;

public interface BreadcrumbBuilder extends Builder<Breadcrumbs> {
	
	StringBuilder getPath();
	
	abstract class ChildCrumbBuilder implements BreadcrumbBuilder {
		
		private final BreadcrumbBuilder parent;
		
		protected ChildCrumbBuilder(BreadcrumbBuilder parent) {
			this.parent = parent;
		}
		
		@Override
		public Breadcrumbs build() {
			return parent.build().append(new Breadcrumb(getName(), getPath().toString()));
		}
		
		@Override
		public StringBuilder getPath() {
			StringBuilder path = parent.getPath();
			if (path.charAt(path.length()-1) != '/' && getUrlSuffix().charAt(0) != '/') {
				path.append('/');
			}
			return path.append(getUrlSuffix());
		}
		
		protected abstract String getName();
		
		protected abstract String getUrlSuffix();
		
	}
	
	class HomeCrumbBuilder implements BreadcrumbBuilder {
		
		private final String baseUrl;
		
		public HomeCrumbBuilder(String baseUrl) {
			this.baseUrl = baseUrl;
		}
		
		@Override
		public Breadcrumbs build() {
			return new Breadcrumbs(new Breadcrumb("Home", getPath().toString()));
		}
		
		public ApiCrumbBuilder withApi(Api api) {
			return new ApiCrumbBuilder(this, api);
		}
		
		@Override
		public StringBuilder getPath() {
			return new StringBuilder(baseUrl);
		}
		
		final class ApiCrumbBuilder extends ChildCrumbBuilder {
			
			private final Api api;
			
			private ApiCrumbBuilder(BreadcrumbBuilder parent, Api api) {
				super(parent);
				this.api = api;
			}
			
			@Override
			protected String getName() {
				return api.getName();
			}
			
			@Override
			protected String getUrlSuffix() {
				return api.getBaseUrl();
			}
			
			public EndpointCrumbBuilder withEndpoint(Endpoint endpoint) {
				return new EndpointCrumbBuilder(this, endpoint);
			}
			
			final class EndpointCrumbBuilder extends ChildCrumbBuilder {
				
				private final Endpoint endpoint;
				
				private EndpointCrumbBuilder(BreadcrumbBuilder parent, Endpoint endpoint) {
					super(parent);
					this.endpoint = endpoint;
				}
				
				@Override
				protected String getName() {
					return endpoint.getName();
				}
				
				@Override
				protected String getUrlSuffix() {
					return endpoint.getName();
				}
			}
			
		}
		
	}
	
}