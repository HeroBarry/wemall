package com.wemall.core.zip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CompressionFilter implements Filter {
    protected Logger log = LoggerFactory.getLogger(CompressionFilter.class);

    @SuppressWarnings("rawtypes")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        boolean compress = false;
        if ((request instanceof HttpServletRequest)){
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            Enumeration headers = httpRequest.getHeaders("Accept-Encoding");
            while (headers.hasMoreElements()){
                String value = (String)headers.nextElement();
                if (value.indexOf("gzip") != -1){
                    compress = true;
                }
            }
        }

        if (compress){
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.addHeader("Content-Encoding", "gzip");
            CompressionResponse compressionResponse = new CompressionResponse(
                httpResponse);
            chain.doFilter(request, compressionResponse);
            compressionResponse.close();
        }else{
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config)
    throws ServletException {
    }

    public void destroy(){
    }
}
