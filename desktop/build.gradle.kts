import org.gradle.internal.os.OperatingSystem

plugins {
	application
	id("com.github.johnrengelman.shadow") version Versions.shadowJar
}

application {
	mainClassName = "${App.packageName}.DesktopLauncherKt"
}

dependencies {
	implementation(project(":core"))
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${Versions.gdx}")
	implementation("com.badlogicgames.gdx:gdx-platform:${Versions.gdx}:natives-desktop")
}

sourceSets {
	main {
		resources.srcDir(rootProject.files("assets"))
	}
}

tasks {
	run.configure {
		workingDir = File(rootProject.file("assets").path)
		isIgnoreExitValue = true

		if (OperatingSystem.current().isMacOsX) {
			// Required to run LWJGL3 Java apps on MacOS
			jvmArgs("-XstartOnFirstThread")
		}
	}

	shadowJar {
		archiveBaseName.set(App.name)
		archiveVersion.set(App.versionName)
		archiveClassifier.set("")
	}
}
