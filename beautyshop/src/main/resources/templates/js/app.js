$(function () {

    $('.header__btn, .nav__link').on('click', function(){
        $('.nav__list').toggleClass('header__top--inner--active');
        $('.header__btn').toggleClass('header__btn--active');
        $('body').toggleClass('lock');
    });

    $('.nav__btn').on('click', function(){
        $('.nav__entrance').toggleClass('nav__entrance--active');
        $('body').toggleClass('lock');
    });
    $('.entrance__btmExit').on('click', function(){
        $('.nav__entrance').toggleClass('nav__entrance--active');
        $('body').toggleClass('lock');
    });

    $('.entrance__btmVhod').on('click', function(){
        $('.card__bottom').toggleClass('card__bottom--active');
        $('.bottom').toggleClass('bottom--active');
    });
});