$('document').ready(function(){
    $( "#topSearchForm" ).submit(function( event ) {
        doSearch('#topSearchQuery',event);
    });
});

function doSearch(id, event){
    $id = $(id);

    if($id.val()){
        var cleaned = encodeURI($id.val().replace(/ /,'+'))
        window.location.href= '/search/' + cleaned;
    }
    if(event != null){
        event.preventDefault();
    }

}