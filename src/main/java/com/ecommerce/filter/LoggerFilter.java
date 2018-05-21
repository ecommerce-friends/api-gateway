package com.ecommerce.filter;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

@Component
public class LoggerFilter extends ZuulFilter {
	
	@Override
	public Object run() throws ZuulException {
		System.out.println("run");
		return null;
	}

	@Override
	public boolean shouldFilter() {
		System.out.println("shouldFilter");
		return true;
	}

	@Override
	public int filterOrder() {
		System.out.println("filterOrder");
		return 0;
	}

	@Override
	public String filterType() {
		System.out.println("filterType");
		return "pre";
	}

}
