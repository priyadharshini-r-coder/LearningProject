import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


abstract  class ExamplePlugin:Plugin<Project> {
    override fun apply(p0: Project) {
        p0.tasks.register("gitVersionProvider",GitversionTask::class.java)
        {
            it.gitVersionOutputFile .set(
               File(p0.buildDir,"intermediates/gitVersionProvider/output")
            )
         it.outputs.upToDateWhen { false }
        }
       /* val androidComponents = p0.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->

            val manifestUpdater =
                p0.tasks.register(variant.name + "ManifestUpdater", ManifestTransformerTask::class.java) {
                    it.gitInfoFile.set(gitVersionProvider.flatMap(GitversionTask::gitVersionOutputFile))
                }
            variant.artifacts.use(manifestUpdater)
                .wiredWithFiles(
                    ManifestTransformerTask::mergedManifest,
                    ManifestTransformerTask::updatedManifest)
                .toTransform(SingleArtifact.MERGED_MANIFEST)

            p0.tasks.register(variant.name + "Verifier", VerifyManifestTask::class.java) {
                it.apkFolder.set(variant.artifacts.get(SingleArtifact.APK))
                it.builtArtifactsLoader.set(variant.artifacts.getBuiltArtifactsLoader())
            }
        }*/
    }

}