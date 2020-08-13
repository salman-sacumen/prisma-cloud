package com.atlassian.bitbucket.server.prisma.servlet;

import java.io.IOException;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.google.common.collect.ImmutableMap;

@Named("RepositoryServlet")
public class RepositoryServlet extends AbstractServlet {

	private static final Logger log = LoggerFactory.getLogger("RepositoryServlet.class");

	@ComponentImport
	private final PluginSettingsFactory pluginSettingsFactory;

	@ComponentImport
	private final RepositoryService repositoryService;

	public RepositoryServlet(@ComponentImport SoyTemplateRenderer soyTemplateRenderer,
			@ComponentImport RepositoryService repositoryService,
			@ComponentImport PluginSettingsFactory pluginSettingsFactory) {

		super(soyTemplateRenderer);
		this.repositoryService = repositoryService;
		this.pluginSettingsFactory = pluginSettingsFactory;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get repoSlug from path
		
		log.info("request thing are parameter name"+req.getParameterNames());
		log.info("messages url are "+req.getParameter("url"));
		String pathInfo = req.getPathInfo();
		String[] components = pathInfo.split("/");

		if (components.length < 3) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Repository repository = repositoryService.getBySlug(components[1], components[2]);

		if (repository == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		PluginSettings pluginSettings = this.pluginSettingsFactory.createSettingsForKey(String.format(Constants.SETTINGS_KEY, components[1],components[2]));
	
		render(resp, "plugin.prisma.repositorySettings", ImmutableMap.<String, Object>of("repository", repository));
	}
	
	protected void doPost() {
		
	}

}
