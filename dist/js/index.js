//react JS
const displayNone = "display__none"


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
                <div className="container_items {displayNone}">
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
                <div className="admin_Container_form display__none">
                    <h2 className="add__rec" id="Redact display__none">Редактирование</h2>
                    <h2 className="add__rec" id="Add">Добавление</h2>
                    <form action="">
                        <div className="input__label">
                            <label className="text__label">Имя:</label>
                            <input type="text" class="input__add" id="input__one" />
                        </div>
                        <div class="input__label">
                            <label className="text__label">Фамилия:</label>
                            <input type="text" class="input__add" id="input__two" />
                        </div>
                        <div class="select__label">
                            <label className="text__label">Специлизация:</label>
                            <select name="specil" id="speciliz">
                                <option value="Парикмахер">парихмахер</option>
                                <option value="Бровист">бровист</option>
                            </select>
                        </div>
                        <div className="button__label">
                            <button className="button__foto">Загрузить фото</button>
                        </div>
                        <div class="time__label display__none">
                            <label className="text__label">Начало:</label>
                            <input type="number" class="int__time" />
                            <label className="text__label">Конец:</label>
                            <input type="number" class="int__time" />
                        </div>
                    </form>
                </div>
                <div className="admin__container__save display__none">
                    <button className="dutton__save">Сохранить</button>
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