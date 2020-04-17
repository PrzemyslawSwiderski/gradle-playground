import java.io.File

object FileUtils {

    fun replaceString(file: File, regex: Regex, newString: String) {
        val text = file.readText().replace(regex, newString)
        file.writeText(text)
    }

}