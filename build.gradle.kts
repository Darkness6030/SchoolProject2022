plugins {
    java
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.Anuken.Arc:arc-core:v138")
    implementation("com.github.Anuken.Arc:backend-sdl:v138")
    implementation("com.github.Anuken.Arc:natives-desktop:v138")
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