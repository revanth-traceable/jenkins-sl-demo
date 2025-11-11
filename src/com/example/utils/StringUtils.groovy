package com.example.utils

class StringUtils implements Serializable {
    
    static String toUpperCase(String text) {
        return text?.toUpperCase() ?: ""
    }
    
    static String formatString(String text, String prefix = "[", String suffix = "]") {
        return "${prefix}${text}${suffix}"
    }
}
