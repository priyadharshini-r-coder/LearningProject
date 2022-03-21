import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract  class GitversionTask: DefaultTask() {
    @get:OutputFile
     abstract val gitVersionOutputFile:RegularFileProperty
    @TaskAction
    fun taskAction()
    {
        //properties are used for input and output


        //this would be the code to get the tip of tree version
        val firstProcess=ProcessBuilder("git","rev-parse --short HEAD").start()
        val error=firstProcess.errorStream.readBytes().toString()
        if(error.isNotBlank())
        {
            System.err.println("Git error :$error")
        }
       // val gitVersion=firstProcess.inputStream.readBytes().toString()
        gitVersionOutputFile.get().asFile.writeText("1234")
    }
}