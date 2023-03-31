String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

function addAnswer(e) {
    e.preventDefault();

    $(".submit-write button[type=submit]").click(addAnswer);

    // 폼의 name 속성을 answer로 변경
    var queryString = $("form[name=answer]").serialize();

    var url = $(".answer-write").attr("action");
    console.log("url : " + url);
    console.log("queryString" + queryString);

    $.ajax({
        type : 'post',
        url : url,
        data : queryString,
        dataType : 'json',
        error: function () {
            alert("error: 에러임");
        },
        success : function (data, status) {
            console.log(data);
            var answerTemplate = $("#answerTemplate").html();
            var template = answerTemplate.format(data.id, data.writer, data.contents, data.time, data.articleId);
            // $(".qna-comment-slipp-articles").prepend(template);
            $("textarea[name=contents]").val("");

            // 새로운 댓글을 마지막 댓글로 추가
            var articles = $(".qna-comment-slipp-articles > article");
            var lastArticle = articles.last();
            var newArticle = $(template);

            // 새로운 댓글을 마지막 댓글로 추가
            lastArticle.after(newArticle);
        }
    });
}