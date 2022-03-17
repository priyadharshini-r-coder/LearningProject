import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ManifestTransformerTask : DefaultTask(){
    @get:InputFile
    abstract  val gitInfoFile:RegularFileProperty

    @get:InputFile
    abstract val  mergeManifest:RegularFileProperty

    @get:OutputFile
    abstract val updatedManifest:RegularFileProperty

    @TaskAction
    fun tasksAction()
    {
        val gitVersion=gitInfoFile.get().asFile.readText()
        var manifest=mergeManifest.asFile.get().readText()
        manifest = manifest.replace("android:versionCode=\"1\"", "android:versionCode=\"${gitVersion}\"")
        println("Writes to " + updatedManifest.get().asFile.getAbsolutePath())
        updatedManifest.get().asFile.writeText(manifest)
    }

}