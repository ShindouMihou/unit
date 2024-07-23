import kotlin.test.Test

class FileOperationsTest {
    @Test
    fun `test file operations`() {
        val test = "./".env()
        test.content = mapOf("hello" to "world")
        println(test.content)

        test.content = mapOf("hello" to "universe")
        println(test.content)

        test.append(mapOf("hi" to "universe", "nihao" to "world"))
        println(test.content)

        // Overwrite existing files on move actions, such as renaming the file,
        // or moving the directory of the file.
        test.overwriteExistingFilesOnMove = true
        test.name = "hello"

        // Results in "<path>/hello.md" instead of "<path>/hello.txt"
        println(test.path)

        // Changes the directory, this will also create the directories if it doesn't exist.
        test.directory = "./tests"
        println(test.path)
    }
}