package Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AllRouteFilter implements Filter {
    private boolean shouldExclude(String requestURI) {
        // Define the routes you want to exclude from filtering
        return requestURI.equals("/assets") || requestURI.equals("/files") || requestURI.equals("/views");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (shouldExclude(request.getRequestURI())){
            filterChain.doFilter(request, response);
        } else{
            if (request.getSession().getAttribute("mess") != null){
                String session_mess = (String) request.getSession().getAttribute("mess");
                request.setAttribute(session_mess.split("\\|")[0], session_mess.split("\\|")[1]);
                request.getSession().removeAttribute("mess");
            }
            filterChain.doFilter(request, response);
        }
    }
}
