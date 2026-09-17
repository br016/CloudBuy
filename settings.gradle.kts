pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral() // ⚠️ OBRIGATÓRIO PARA O SUPABASE
    }
}

rootProject.name = "CloudBuy"
include(":app")