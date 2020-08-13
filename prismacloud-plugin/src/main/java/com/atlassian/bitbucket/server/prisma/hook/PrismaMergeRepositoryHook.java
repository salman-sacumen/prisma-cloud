package com.atlassian.bitbucket.server.prisma.hook;

import com.atlassian.bitbucket.hook.repository.RepositoryMergeRequestCheck;
import com.atlassian.bitbucket.hook.repository.RepositoryMergeRequestCheckContext;
import com.atlassian.bitbucket.pull.PullRequestParticipant;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.setting.*;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrismaMergeRepositoryHook implements RepositoryMergeRequestCheck, RepositorySettingsValidator
{
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");
    private static final Logger log = LoggerFactory.getLogger(PrismaMergeRepositoryHook.class);

    /**
     * Vetos a pull-request if there aren't enough reviewers.
     */
    @Override
    public void check(RepositoryMergeRequestCheckContext context)
    {
    
		log.error("settings " + context.getSettings().toString());
		log.error("settings " + context.getSettings().getString("allowedhighcount", "0").trim());
		
//        int requiredReviewers = Integer.parseInt(context.getSettings().getString("reviewers"));
//        int acceptedCount = 0;
//        for (PullRequestParticipant reviewer : context.getMergeRequest().getPullRequest().getReviewers())
//        {
//            acceptedCount = acceptedCount + (reviewer.isApproved() ? 1 : 0);
//        }
//        if (acceptedCount < requiredReviewers)
//        {
//            context.getMergeRequest().veto("Not enough approved reviewers", acceptedCount + " reviewers have approved your pull request. You need " + requiredReviewers + " (total) before you may merge.");
//        }
    }

    @Override
    public void validate(Settings settings, SettingsValidationErrors errors, Repository repository)
    {
    	String numReviewersString = settings.getString("allowedhighcount", "0").trim();

		if (!NUMBER_PATTERN.matcher(numReviewersString).matches()) {
			errors.addFieldError("allowedhighcount", "Enter a number");

		} else if (Integer.parseInt(numReviewersString) <= 0) {
			errors.addFieldError("allowedhighcount", "Number of reviewers must be greater than zero");
		}
		errors.addFieldError("allowedhighcount", numReviewersString);
		
//        String numReviewersString = settings.getString("reviewers", "0").trim();
//        if (!NUMBER_PATTERN.matcher(numReviewersString).matches())
//        {
//            errors.addFieldError("reviewers", "Enter a number");
//        }
//        else if (Integer.parseInt(numReviewersString) <= 0)
//        {
//            errors.addFieldError("reviewers", "Number of reviewers must be greater than zero");
//        }
    }

}
