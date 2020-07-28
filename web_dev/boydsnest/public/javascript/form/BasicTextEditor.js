function wrapText(tag) {
    var textArea = document.getElementById("BasicTextEditor_target");
    var range = getInputSelection(textArea);
    textArea.value =
        textArea.value.substring(0, range.start)
            + "[" + tag + "]" + textArea.value.substring(range.start, range.end) + "[/" + tag + "]" +
        textArea.value.substring(range.end, textArea.value.length);

}
function insertText(tag) {
    var textArea = document.getElementById("BasicTextEditor_target");
    var pos = getCaret(textArea);
    textArea.value =
        textArea.value.substring(0, pos)
            + "[" + tag + "]" +
        textArea.value.substring(pos, textArea.value.length);
}
function getCaret(el) {
    if (el.selectionStart) {
        return el.selectionStart;
    } else if (document.selection) {
        el.focus();

        var r = document.selection.createRange();
        if (r == null) {
          return 0;
        } 

        var re = el.createTextRange(),
            rc = re.duplicate();
        re.moveToBookmark(r.getBookmark());
        rc.setEndPoint('EndToStart', re);

        return rc.text.length;
    }
    return 0;
}
function getInputSelection(el) {
    var start = 0, end = 0, normalizedValue, range,
        textInputRange, len, endRange;

    if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
        start = el.selectionStart;
        end = el.selectionEnd;
    } else {
        range = document.selection.createRange();

        if (range && range.parentElement() == el) {
            len = el.value.length;
            normalizedValue = el.value.replace(/\r\n/g, "\n");

            // Create a working TextRange that lives only in the input
            textInputRange = el.createTextRange();
            textInputRange.moveToBookmark(range.getBookmark());

            // Check if the start and end of the selection are at the very end
            // of the input, since moveStart/moveEnd doesn't return what we want
            // in those cases
            endRange = el.createTextRange();
            endRange.collapse(false);

            if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
                start = end = len;
            } else {
                start = -textInputRange.moveStart("character", -len);
                start += normalizedValue.slice(0, start).split("\n").length - 1;

                if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
                    end = len;
                } else {
                    end = -textInputRange.moveEnd("character", -len);
                    end += normalizedValue.slice(0, end).split("\n").length - 1;
                }
            }
        }
    }

    if (start == 0 && end == 0){
        start = end = getCaret(el);
    }

    return {
        start: start,
        end: end
    };
}
