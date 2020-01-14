package dev.tricht.lunaris;

import lombok.extern.slf4j.Slf4j;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.OS;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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
        Properties properties = new Properties();
        properties.load(AutoUpdateConfiguration.class.getResourceAsStream("/lunaris.properties"));
        File imageDir = new File("target/image");
        List<FileMetadata.Reference> files = FileMetadata.streamDirectory(imageDir.getAbsolutePath())
                .map(reference -> {
                            String path = target.toPath()
                                    .toAbsolutePath()
                                    .relativize(reference.getSource().toAbsolutePath())
                                    .toString();
                            return reference.os(OS.WINDOWS)
                                    .path("${user.dir}/" + path)
                                    .ignoreBootConflict(true)
                                    .uri("https://storage.googleapis.com/lunaris/image-windows/"
                                            + path.replaceAll("\\\\", "/")
                                                .replace("image/", ""));
                        }
                )
                .collect(Collectors.toList());
        files.add(shadedJar.get()
                .uri(String.format(
                        "https://github.com/mtricht/lunaris/releases/download/v%s/lunaris-%s.jar",
                        properties.get("version"),
                        properties.get("version")
                ))
                .classpath(true)
                .path("${user.dir}/app/lunaris.jar")
                .ignoreBootConflict(true));
        Configuration build = Configuration.builder()
                .property("default.launcher.main.class", "dev.tricht.lunaris.Lunaris")
                .files(files)
                .build();

        FileWriter writer = new FileWriter("autoupdater.xml");
        build.write(writer);
        writer.flush();
        writer.close();
    }
}
