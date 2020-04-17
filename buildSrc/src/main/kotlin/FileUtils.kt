import java.io.File

fun File.replaceString(regex: Regex, newString: String) {
    val text = this.readText().replace(regex, newString)
    this.writeText(text)
}