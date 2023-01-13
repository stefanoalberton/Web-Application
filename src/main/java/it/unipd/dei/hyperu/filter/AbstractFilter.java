package it.unipd.dei.hyperu.filter;

import it.unipd.dei.hyperu.resource.User;
import it.unipd.dei.hyperu.resource.ResultMessage;
import it.unipd.dei.hyperu.utils.ServletFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Abstract class for the filters.
 *
 * @author Luca Martinelli (luca.martinelli.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractFilter extends HttpFilter {

    /**
     * The logger.
     */
    Logger logger;

    /**
     * @param config Configs of the filter, if any
     * @throws ServletException If ServletException
     */
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        logger = LogManager.getLogger(this.getClass());
    }

    protected void sendError(HttpServletResponse res, ResultMessage message) throws IOException {
        ServletFunctions.writeResult(res, logger, message, null);
    }

    protected String[] getTokens(HttpServletRequest req, String path) {
        return ServletFunctions.getTokens(req, path);
    }

    protected User getLoggedUser(HttpServletRequest req) {
        return ServletFunctions.getLoggedUser(req);
    }
}
