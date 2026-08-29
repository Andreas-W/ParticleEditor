# Generals Particle Editor

Editor for Command & Conquer Generals / Zero Hour `ParticleSystem.ini` and `FXList.ini`
files, with a live Java 3D preview of the effect.

## Requirements

* **JDK 22 or newer.** The build targets release 22.
* Maven — or just use the bundled wrapper (`mvnw` / `mvnw.cmd`), which downloads it.

Everything else comes from Maven Central. There is no longer a local Java3D install to
set up.

`mvnw` builds with whatever `JAVA_HOME` points at, falling back to `java` on the `PATH`.
If that is an older JDK the `release 22` compile fails, so set it explicitly:

```
set JAVA_HOME=C:\Program Files\Java\jdk-22
```

This only affects command-line builds. IntelliJ uses its own Project SDK for both
compilation and its bundled Maven runner.

## Build

```
mvnw clean package
```

That produces `target/particle-editor-<version>.jar` plus its dependencies in
`target/lib/`.

## Run

From the working copy:

```
mvnw exec:exec
```

Or from the packaged jar (keep it next to its `lib/` folder):

```
java -jar target/particle-editor-2.0.0-SNAPSHOT.jar
```

The editor reads `config.properties` from the **current working directory**, so run it
from a directory that has one. Point `ParticleSystemFile`, `FXListFile` and
`TextureFolder1` / `TextureFolder2` at your game data.

### The `--add-opens` flag

JOGL reaches into `sun.awt` internals to attach an OpenGL context to the AWT canvas.
Java 16 made that illegal by default and Java 20 made it fatal, so the run needs:

```
--add-opens java.desktop/sun.awt=ALL-UNNAMED
```

Both `mvnw exec:exec` and the executable jar (via its `Add-Opens` manifest attribute)
already pass it. You only need it by hand if you launch `main.Main` off a raw classpath,
for example from an IDE run configuration.

## Dependencies

| What | Version | Note |
| --- | --- | --- |
| [JogAmp Java 3D](https://jogamp.org/) | 1.7.2 | Community continuation of Java3D 1.5.2. Same API, packages renamed `javax.media.j3d` -> `org.jogamp.java3d` and `javax.vecmath` -> `org.jogamp.vecmath`. |
| JOGL / GlueGen / JOAL | 2.6.0 | Native OpenGL and OpenAL bindings Java 3D renders through. Natives for Windows, Linux and macOS on x86-64 are all on the classpath. |
| MigLayout Swing | 11.4.3 | Replaces the old bundled `miglayout15-swing.jar`. |

The TGA and DDS image readers (`com.realityinteractive.imageio.tga`, `net.nikr.dds`)
are vendored in `src/` and are not Maven dependencies.

## IntelliJ IDEA

Open the project **as a Maven project**: `File > Open`, select `pom.xml`, then *Open as
Project*. The classpath comes entirely from the POM. Set the Project SDK to JDK 22 or
newer; the language level follows `maven.compiler.release`.

A **Particle Editor** run configuration is committed in `.run/` and shows up in the run
dropdown automatically. If you build one by hand instead, it needs:

* VM options: `--add-opens java.desktop/sun.awt=ALL-UNNAMED` (see above — IntelliJ
  launches `main.Main` off a raw classpath, so neither the jar manifest nor
  `exec:exec` supplies the flag for you)
* Working directory: the repo root, which is where `config.properties`, `ground.jpg`
  and `models/` live

The project no longer carries Eclipse `.classpath` / `.project` / `.settings` files.
They described a Java3D 1.5.2 install on a fixed `H:\` path and were superseded by the
POM.
