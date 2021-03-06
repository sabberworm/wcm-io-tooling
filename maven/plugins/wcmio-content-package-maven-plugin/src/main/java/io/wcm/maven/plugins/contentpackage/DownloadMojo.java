/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.maven.plugins.contentpackage;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import io.wcm.tooling.commons.packmgr.download.PackageDownloader;
import io.wcm.tooling.commons.packmgr.unpack.ContentUnpacker;
import io.wcm.tooling.commons.packmgr.unpack.ContentUnpackerProperties;

/**
 * Builds and downloads a content package defined on a remote CRX or AEM system.
 */
@Mojo(name = "download", defaultPhase = LifecyclePhase.INSTALL, requiresProject = false, requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public final class DownloadMojo extends AbstractContentPackageMojo {

  /**
   * The output file to save.
   */
  @Parameter(property = "vault.outputFile", required = true, defaultValue = "${project.build.directory}/${project.build.finalName}.zip")
  private String outputFile;

  /**
   * If set to true the package is unpacked to the directory specified by <code>unpackDirectory </code>.
   */
  @Parameter(property = "vault.unpack", defaultValue = "false")
  private boolean unpack;

  /**
   * Directory to unpack the content of the package to.
   */
  @Parameter(property = "vault.unpackDirectory", defaultValue = "${basedir}")
  private File unpackDirectory;

  /**
   * If unpack=true: delete existing content from the named directories (relative to <code>unpackDirectory</code> root)
   * before unpacking the package content, to make sure only the content from the downloaded package remains.
   */
  @Parameter
  @SuppressWarnings("PMD.ImmutableField")
  private String[] unpackDeleteDirectories = new String[] {
      "jcr_root",
      "META-INF"
  };

  /**
   * List of regular patterns matching relative path of extracted content package. All files matching these patterns
   * are excluded when unpacking the content package.
   */
  @Parameter
  private String[] excludeFiles;

  /**
   * List of regular patterns matching node paths inside a <code>.content.xml</code> file. All nodes matching
   * theses patterns are removed from the <code>.content.xml</code> when unpacking the content package.
   */
  @Parameter
  private String[] excludeNodes;

  /**
   * List of regular patterns matching property names inside a <code>.content.xml</code> file. All properties matching
   * theses patterns are removed from the <code>.content.xml</code> when unpacking the content package.
   */
  @Parameter
  private String[] excludeProperties;

  /**
   * List of regular patterns matching mixin names inside a <code>.content.xml</code> file. All mixins matching
   * theses patterns are removed from the <code>.content.xml</code> when unpacking the content package.
   */
  @Parameter
  private String[] excludeMixins;

  /**
   * Downloads the files
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isSkip()) {
      return;
    }

    PackageDownloader downloader = new PackageDownloader(getPackageManagerProperties(), getLoggerWrapper());

    File outputFileObject = downloader.downloadFile(getPackageFile(), this.outputFile);
    if (this.unpack) {
      unpackFile(outputFileObject);
    }
  }

  /**
   * Unpack content package
   */
  private void unpackFile(File file) throws MojoExecutionException {

    // initialize unpacker to validate patterns
    ContentUnpackerProperties props = new ContentUnpackerProperties();
    props.setExcludeFiles(this.excludeFiles);
    props.setExcludeNodes(this.excludeNodes);
    props.setExcludeProperties(this.excludeProperties);
    props.setExcludeMixins(this.excludeMixins);
    ContentUnpacker unpacker = new ContentUnpacker(props);

    // validate output directory
    if (this.unpackDirectory == null) {
      throw new MojoExecutionException("No unpack directory specified.");
    }
    if (!this.unpackDirectory.exists()) {
      this.unpackDirectory.mkdirs();
    }

    // remove existing content
    if (this.unpackDeleteDirectories != null) {
      for (String directory : unpackDeleteDirectories) {
        File directoryFile = FileUtils.getFile(this.unpackDirectory, directory);
        if (directoryFile.exists()) {
          if (!deleteDirectoryWithRetries(directoryFile, 0)) {
            throw new MojoExecutionException("Unable to delete existing content from "
                + directoryFile.getAbsolutePath());
          }
        }
      }
    }

    // unpack file
    unpacker.unpack(file, this.unpackDirectory);

    getLog().info("Package unpacked to " + this.unpackDirectory.getAbsolutePath());
  }

  /**
   * Delete fails sometimes or may be blocked by an editor - give it some time to try again (max. 1 sec).
   */
  private boolean deleteDirectoryWithRetries(File directory, int retryCount) {
    if (retryCount > 100) {
      return false;
    }
    if (FileUtils.deleteQuietly(directory)) {
      return true;
    }
    else {
      try {
        Thread.sleep(10);
      }
      catch (InterruptedException ex) {
        // ignore
      }
      return deleteDirectoryWithRetries(directory, retryCount + 1);
    }
  }

}
