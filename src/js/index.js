//react JS
const displayNone = "display__none"
const e = React.createElement;
let checkClick = 0;

const adminRecord = () => {
    const [arr, setarr] = React.useState();
        return(
        <div className="admin__container">
            <div className="admin__container__button">
                <button className="button__item">Мастер</button>
                <button className="button__item">Услуги</button>
                <button className="button__item">Специализации</button>
                <button className="button__item">Изменить время работы</button>
            </div>
            <div className="container_items ">
                <div className="card__container" id="cardContainerMasters">
                    <h1 className="title_record">Мастера</h1>
                    <div className="record_item" id="record__item__master">
                        
                    </div>
                    <button className="masters__button__add">Создать нового мастера</button>
                </div>
            </div>
            <div className="admin_Container_form">
                <h2 className="add__rec display__none" id="Redact">Редактирование</h2>
                <h2 className="add__rec" id="Add">Добавление</h2>
                <form action="">
                    <div className="input__label">
                        <label className="text__label">Имя:</label>
                        <input type="text" className="input__add" id="input__one" />
                    </div>
                    <div className="input__label">
                        <label className="text__label">Фамилия:</label>
                        <input type="text" className="input__add" id="input__two"/>
                    </div>
                    <div className="select__label">
                        <label className="text__label">Специлизация:</label>
                        <select name="specil" id="speciliz" >
                            <option value="Парикмахер">парихмахер</option>
                            <option value="Бровист">бровист</option>
                        </select>
                    </div>
                    <div className="button__label">
                        <button className="button__foto">Загрузить фото</button>
                    </div>
                    {/* <div class="time__label display__none">
                        <label className="text__label">Начало:</label>
                        <input type="number" class="int__time" />
                        <label className="text__label">Конец:</label>
                        <input type="number" class="int__time" />
                    </div> */}
                </form>
            </div>
            <div className="admin__container__save">
                <button className="dutton__save" onClick={saveClick} id = "button__save">Сохранить</button>
            </div>  
        </div>   
    )
}

function includeCardMaster () {
    checkClick++
    let name = document.getElementById("input__one").value
    let fio = document.getElementById("input__two").value
    let spec = document.getElementById("speciliz").value
        return(
            <div className="card__masters" id={checkClick}>
	            <img src="#" alt="" className="card__masters__img"/>
	            <div className="card__masters_text">
		            <h3 className="card__masters__name">{name} {fio}</h3>
		            <div className="spec__container">
			            <h4 className="Card__masters__spec">{spec}</h4>
		            </div>
	            </div>
            </div>
        )
    }

const saveClick = () => {
    return(
        ReactDOM.render(e(includeCardMaster), document.getElementById("record__item__master"))
    )
};


// const app = ReactDOMClient.createRoot(document.getElementById("adminRecordReact"));

// app.render(<adminRecord />);


const domContainer = document.getElementById("adminRecordReact");
ReactDOM.render(e(adminRecord), domContainer);