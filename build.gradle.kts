plugins {
    java
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    val arcVersion = "v139"

    implementation("com.github.Anuken.Arc:arc-core:$arcVersion")
    implementation("com.github.Anuken.Arc:backend-sdl:$arcVersion")
    implementation("com.github.Anuken.Arc:freetype:$arcVersion")

    implementation("com.github.Anuken.Arc:natives-desktop:$arcVersion")
    implementation("com.github.Anuken.Arc:natives-freetype-desktop:$arcVersion")

    implementation("com.github.Anuken.Mindustry:core:$arcVersion")
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    manifest {
        attributes["Main-Class"] = "dark.Main"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}