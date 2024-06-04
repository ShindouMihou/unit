package pw.mihou.unit.exceptions

class PathAlreadyTakenException(path: String):
    RuntimeException("The destination path $path already exists. " +
            "If you want to overwrite the file, set `overwriteExistingFilesOnMove` to `true` on the Unit instance.")