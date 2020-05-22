package nl.lexemmens.podman.image;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageConfigurationTest {

    @Test
    void testEmptyImageConfiguration() throws MojoExecutionException {
        // Tags cannot be empty. If tagging should not be done it should be skipped by specifying the appropriate parameter
        // For saving and pushing everything is required
        ImageConfiguration ic = new ImageConfiguration(null, null, null, false);
        Assertions.assertNull(ic.getRegistry());
        Assertions.assertFalse(ic.getImageHash().isPresent());
        Assertions.assertThrows(MojoExecutionException.class, ic::getFullImageNames);
    }

    @Test
    void testTagWithoutVersionCausesException() throws MojoExecutionException {
        // No version is specified as well as createImageTaggedLatest is set to false. Should fail because no version at all is available

        ImageConfiguration ic = new ImageConfiguration(null, new String[]{"exampleTag"}, null, false);
        Assertions.assertEquals("exampleTag", ic.getRegistry()); // Registry is allowed to be part of the tag
        Assertions.assertFalse(ic.getImageHash().isPresent());
        Assertions.assertThrows(MojoExecutionException.class, ic::getFullImageNames);
    }

    @Test
    void testTagWithLatestTagCausesNoException() throws MojoExecutionException {
        // No version is specified as well as createImageTaggedLatest is set to false. Should fail because no version at all is available

        ImageConfiguration ic = new ImageConfiguration(null, new String[]{"exampleTag"}, null, true);
        Assertions.assertEquals("exampleTag", ic.getRegistry()); // Registry is allowed to be part of the tag
        Assertions.assertFalse(ic.getImageHash().isPresent());
        Assertions.assertDoesNotThrow(ic::getFullImageNames);
    }

    @Test
    void testTagWithVersionAndNoLatestTagCausesNoException() throws MojoExecutionException {
        // No version is specified as well as createImageTaggedLatest is set to false. Should fail because no version at all is available

        ImageConfiguration ic = new ImageConfiguration(null, new String[]{"exampleTag"}, "1.0.0", false);
        Assertions.assertEquals("exampleTag", ic.getRegistry()); // Registry is allowed to be part of the tag
        Assertions.assertFalse(ic.getImageHash().isPresent());
        Assertions.assertDoesNotThrow(ic::getFullImageNames);
    }

    @Test
    void testRepoInTagOnly() throws Exception
    {
        ImageConfiguration ic = new ImageConfiguration(null, new String[]{"registry.example.org:1234/repo/tag"}, "0.0.1", false);
        Assertions.assertNotNull(ic.getRegistry());
        Assertions.assertEquals("registry.example.org:1234", ic.getRegistry());
        Assertions.assertFalse(ic.getImageHash().isPresent());

        Assertions.assertDoesNotThrow(ic::getFullImageNames);
    }
}
