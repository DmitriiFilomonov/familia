$(function () {

    $(".menu a, .menu__item a, .footer a").on("click", function (event) {
        event.preventDefault();
        var id  = $(this).attr('href'),
            top = $(id).offset().top;
        $('body,html').animate({scrollTop: top}, 1500);
    });


    $(window).scroll(function(){
        if ($(window).scrollTop() > 100) {
            $('.header__top').addClass('header__top--fix');
        }
        else {
            $('.header__top').removeClass('header__top--fix');
        }
    });
    
    $('.header__btn, .menu__link').on('click', function(){
        $('.menu__list').toggleClass('header__top--inner--active');
        $('.header__btn').toggleClass('header__btn--active');
        $('body').toggleClass('lock');
    });

    var mixer = mixitup('.portfolio__content');
});