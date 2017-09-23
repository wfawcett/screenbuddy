//"use strict";
var _tmdb = {};

_tmdb.doMovieSearch = function(query){
    var restCall = ['https://api.themoviedb.org/3/search/movie?api_key=274472d0b063eec06615cfed6a703b95&language=en-US&query=',encodeURIComponent(query), '&page=1&include_adult=false'].join('');
    console.log(restCall);
    $.ajax({
        url: restCall,
        timeout: 10000, // 10 second timout
        success: function(data){_tmdb.querySuccess(data);},
        error: function(err){_tmdb.queryError(err);}
    });
};

_tmdb.querySuccess = function(response){
    console.log(response);
};


_tmdb.queryError = function(error){
    console.log(error);
};