import pw.mihou.unit.txt
import kotlin.test.Test

class FileOperationsTest {
    @Test
    fun `test file operations`() {
        val test = "./test".txt
        test.content = "Hello world!"

        // By default, this will append a newline.
        // You can disable it adding:
        // test.appendNewLineOnPlusAssign = false
        test.append("Nihao!")

        // You can also use this, but it will overwrite the entire file content instead:
        test.content += "\nMore than just friends."

        println(test.content)

        // Overwrite existing files on move actions, such as renaming the file,
        // or moving the directory of the file.
        test.overwriteExistingFilesOnMove = true
        test.name = "hello"

        test.extension = "md"

        // Results in "<path>/hello.md" instead of "<path>/hello.txt"
        println(test.path)

        // Changes the directory, this will also create the directories if it doesn't exist.
        test.directory = "./tests"
        println(test.path)
    }
}