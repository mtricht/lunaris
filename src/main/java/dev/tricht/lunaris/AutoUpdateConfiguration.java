package dev.tricht.lunaris;

import lombok.extern.slf4j.Slf4j;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.OS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AutoUpdateConfiguration {
    public static void main(String[] args) throws IOException {
        File target = new File("target");
        Optional<FileMetadata.Reference> shadedJar = FileMetadata.streamDirectory(target.getAbsolutePath())
                .filter(r -> r.getSource().toString().endsWith("-shaded.jar"))
                .findFirst();
        if (!shadedJar.isPresent()) {
            log.error("Shaded jar not found.");
            System.exit(1);
            return;
        }
        FileMetadata.Reference shadedJarFile = shadedJar.get()
                // TODO: get version from POM
                .uri("https://github.com/mtricht/lunaris/releases/download/v0.3.2/lunaris-0.3.2.jar")
                .classpath(true)
                .path("lunaris.jar")
                .os(OS.WINDOWS)
                .ignoreBootConflict(true);
        Configuration build = Configuration.builder()
                .basePath("${user.dir}/app")
                .property("default.launcher.main.class", "dev.tricht.lunaris.Lunaris")
                .files(List.of(shadedJarFile))
                .build();

        FileWriter writer = new FileWriter("autoupdater.xml");
        build.write(writer);
        writer.flush();
        writer.close();
    }
}
