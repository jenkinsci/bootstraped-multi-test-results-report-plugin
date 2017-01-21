package com.github.bogdanlivadariu.jenkins.reporting;

import hudson.model.Run;
import jenkins.model.RunAction2;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SafeArchiveServingRunAction extends SafeArchiveServingAction implements RunAction2 {

    private static final Logger LOGGER = Logger.getLogger(SafeArchiveServingRunAction.class.getName());

    public SafeArchiveServingRunAction(File rootDir, String urlName, String indexFile, String iconName, String title,
        String... safeExtensions) {
        super(rootDir, urlName, indexFile, iconName, title, safeExtensions);
    }

    @Override
    public void onAttached(Run<?, ?> r) {
        try {
            processDirectory();
        } catch (IOException | NoSuchAlgorithmException ex) {
            LOGGER.log(Level.WARNING, "Exception scanning " + r.getRootDir(), ex);
        }
    }

    @Override
    public void onLoad(Run<?, ?> r) {

    }
}
