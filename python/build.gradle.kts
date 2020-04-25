import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java // apply java convention to automatically locate sources in intellij
    idea
    id("com.jetbrains.python.envs") version "0.0.30"
}

val pythonEnvsDirName = ".pythonEnvs"
val pythonBuildDir = project.projectDir.resolve(pythonEnvsDirName)
val mainSources = project.projectDir.resolve("main")
val testSources = project.projectDir.resolve("test")

idea {
    module {
        sourceDirs = mutableSetOf(mainSources)
        testSourceDirs = mutableSetOf(testSources)
        excludeDirs = mutableSetOf(pythonBuildDir)
    }
}

envs {
    bootstrapDirectory = file(pythonBuildDir).resolve("bootstrap")
    envsDirectory = file(pythonBuildDir).resolve("envs")

    python("python-3.8.2", "3.8.2")
    virtualenv("virtualenv-3.8.2", "python-3.8.2")
}
//tasks.named("build_envs") {
//    dependsOn("addPythonEnvsToIgnore")
//}
tasks {
    build {
        dependsOn("build_envs")
    }
    register<Exec>("pythonRun") {
        group = "python"
        executable = envs.pythonPath()
        workingDir = mainSources
        args("numpy_test.py")
        dependsOn("pipInstall")
        doFirst {
            val tasks = project.tasks.asMap
            println(tasks)
        }
    }

    register<Exec>("pythonTestsRun") {
        group = "python"
        executable = envs.pythonPath()
        workingDir = file("test")
        args("test_quicksort.py")
        environment("PYTHONPATH", mainSources)
        dependsOn("pipInstall")
    }

    register<Exec>("pipInstall") {
        group = "python"
        executable = envs.pipPath()
        args("install", "-r", "requirements.txt")

        dependsOn("build_envs")
    }

//    val addPythonEnvsToIgnore = registering {
    register("addPythonEnvsToIgnore") {
        group = "python"
        doFirst {
            val tasks = tasks
            val gitignore = file(projectDir).resolve(".gitignore").also { it.createNewFile() }
            if (gitignore.readLines().none { it.contains(pythonEnvsDirName) })
                gitignore.writeText(pythonEnvsDirName)
        }
    }
}

fun com.jetbrains.python.envs.PythonEnvsExtension.pythonPath(): String {
    val path = this.virtualEnvs.first().envDir.absolutePath
    return if (Os.isFamily(Os.FAMILY_WINDOWS))
        "$path\\Scripts\\python.exe"
    else
        "$path\\Scripts\\python"
}

fun com.jetbrains.python.envs.PythonEnvsExtension.pipPath(): String {
    val path = this.virtualEnvs.first().envDir.absolutePath
    return if (Os.isFamily(Os.FAMILY_WINDOWS))
        "$path\\Scripts\\pip.exe"
    else
        "$path\\Scripts\\pip"
}