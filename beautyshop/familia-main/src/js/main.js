// import adminRecord from 'ReactCompinents/adminRecord'
// import React from "react"
// import * as ReactDOMClient from 'react-dom/client';

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

//слаидер
$('.slider-for').slick({
  slidesToShow: 1,
  slidesToScroll: 1,
  arrows: false,
  fade: true,
  autoplay: true,
  autoplaySpeed: 5000,
  asNavFor: '.slider-nav',
  swipeToSlide: true,
  touchThreshold: 5000
});
$('.slider-nav').slick({
  slidesToShow: 7,
  slidesToScroll: 1,
  asNavFor: '.slider-for',
  dots: true,
  arrows: false,
  centerMode: true,
  focusOnSelect: true,
  touchThreshold: 5000,
  swipeToSlide: true,
  responsive: [
    {
      breakpoint: 768,
      settings: {
        slidesToShow: 3
      }
    }
  ]
});

//модальное окно
document.addEventListener("DOMContentLoaded", function(){
  var scrollbar = document.body.clientWidth - window.innerWidth + 'px';
  console.log(scrollbar);
  document.querySelector('[href="#openModal"]').addEventListener('click',function(){
    document.body.style.overflow = 'hidden';
    document.querySelector('#openModal').style.marginLeft = scrollbar;
  });
  document.querySelector('[href="#close"]').addEventListener('click',function(){
    document.body.style.overflow = 'visible';
    document.querySelector('#openModal').style.marginLeft = '0px';
  });
});


//react JS



class adminRecord extends React.Component{
    render() {
        return(
            <div className="admin__container">
                <div className="admin__container__button">
                    <button className="button__item">Мастер</button>
                    <button className="button__item">Услуги</button>
                    <button className="button__item">Специализации</button>
                    <button className="button__item">Изменить время работы</button>
                </div>
                <div className="container_items display__none">
                    <div className="card__container" id="cardContainerMasters">
                       <h1 className="title_record">Мастера</h1>
                        <div className="record_item">
                            @@include('html/Record/card/_cardMasters.html', {})
                            @@include('html/Record/card/_cardMasters.html', {})
                            @@include('html/Record/card/_cardMasters.html', {})
                        </div>
                        <button class="masters__button__add">Создать нового мастера</button>
                    </div>
                </div>
                <div class="admin_Container_form display__none">
                    <h2 class="add__rec" id="Redact display__none">Редактирование</h2>
                    <h2 class="add__rec" id="Add">Добавление</h2>
                    <form action="">
                        <div class="input__label">
                            <label class="text__label">Имя:</label>
                            <input type="text" class="input__add" id="input__one" />
                        </div>
                        <div class="input__label">
                            <label class="text__label">Фамилия:</label>
                            <input type="text" class="input__add" id="input__two" />
                        </div>
                        <div class="select__label">
                            <label class="text__label">Специлизация:</label>
                            <select name="specil" id="speciliz">
                                <option value="Парикмахер">парихмахер</option>
                                <option value="Бровист">бровист</option>
                            </select>
                        </div>
                        <div class="button__label">
                            <button class="button__foto">Загрузить фото</button>
                        </div>
                        <div class="time__label display__none">
                            <label class="text__label">Начало:</label>
                            <input type="number" class="int__time" />
                            <label class="text__label">Конец:</label>
                            <input type="number" class="int__time" />
                        </div>
                    </form>
                </div>
                <div class="admin__container__save display__none">
                    <button class="dutton__save">Сохранить</button>
                </div>  
            </div>   
        );
    }
}

// const app = ReactDOMClient.createRoot(document.getElementById("adminRecordReact"));

// app.render(<adminRecord />);

const e = React.createElement;
const domContainer = document.getElementById("adminRecordReact");
ReactDOM.render(e(adminRecord), domContainer);