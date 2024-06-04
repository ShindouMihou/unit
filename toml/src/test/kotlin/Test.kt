import kotlinx.serialization.Serializable
import pw.mihou.unit.toml
import kotlin.test.Test

@Serializable
data class Hello(val name: String)
class FileOperationsTest {
    @Test
    fun `test file operations`() {
        val test = "./test".toml<Hello>()
        test.content = Hello("world")
        println(test.content)

        // Overwrite existing files on move actions, such as renaming the file,
        // or moving the directory of the file.
        test.overwriteExistingFilesOnMove = true
        test.name = "hello"

        test.extension = "toml"

        // Results in "<path>/hello.md" instead of "<path>/hello.txt"
        println(test.path)

        // Changes the directory, this will also create the directories if it doesn't exist.
        test.directory = "./tests"
        println(test.path)
    }
}