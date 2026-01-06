plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "kotlin-template"

includeDirectory("app")
includeDirectory("libraries")

fun includeDirectory(path: String) = rootDir.resolve(path)
    .walk()
    .filter { it.name.startsWith("build.gradle") }
    .forEach {
        val directory = it.parentFile
        val projectPath = ":${directory.name}"
        include(projectPath)
        project(projectPath).projectDir = directory
    }