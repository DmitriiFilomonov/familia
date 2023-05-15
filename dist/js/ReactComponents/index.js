const displayNone = "display__none"
const e = React.createElement;
let checkClick = 0;



class Users extends React.Component{
    render(){
        if(this.props.user.length < 1 )
            return(
                <h1>Мастера не найдены</h1>
            )
        else
            return(
                <div>
                    {this.props.user.map((el) => (
                        <div className="card__masters" key={el.id}>
                            <img src="#" alt="" className="card__masters__img"/>
                            <div className="card__masters_text">
                                <h3 className="card__masters__name">{el.firstname} {el.lastname}</h3>
                                <div className="spec__container">
                                    <h4 className="Card__masters__spec">{el.spec}</h4>
                                </div>
                            </div>
                        </div>
                    )
                    )
                    }
                </div>
            )
    }

}


class AddMasters extends React.Component{
    constructor(props){
        super(props)
        this.state = {
            firstname: "",
            lastname: "",
            spec: ""
        }
    }
    render() {
        return(
            <div className="admin_Container_form">
                <h2 className="add__rec display__none" id="Redact">Редактирование</h2>
                <h2 className="add__rec" id="Add">Добавление</h2>
                <form action="">
                    <div className="input__label">
                        <label className="text__label">Имя:</label>
                        <input type="text" className="input__add" id="input__one" onChange={(e) => this.setState({firstname: e.target.value})} />
                    </div>
                    <div className="input__label">
                        <label className="text__label">Фамилия:</label>
                        <input type="text" className="input__add" id="input__two" onChange={(e) => this.setState({lastname: e.target.value})}/>
                    </div>
                    <div className="select__label">
                        <label className="text__label" >Специлизация:</label>
                        <select name="specil" id="speciliz" onChange={(e) => this.setState({spec: e.target.value})}>
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
                <div className="admin__container__save">
                    <button className="dutton__save" onClick={() => this.props.onAdd({
                        firstname: this.state.firstname,
                        lastname: this.state.lastname,
                        spec: this.state.spec
                    })} id = "button__save">Сохранить</button>
                </div>
            </div>
        )
    }

}



class adminRecord extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            user: [
                {
                    id: 1,
                    firstname: 'olga',
                    lastname: 'Marr',
                    spec: 'Парикмахер'
                },
                {
                    id: 2,
                    firstname: 'odfgdfxx',
                    lastname: 'dfgdfg',
                    spec: 'Парикмахер'
                }
            ]
        }
        this.addUser = this.addUser.bind(this)
    }
    render(){
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
                        <Users user = {this.state.user}/>
                    </div>
                    <button className="masters__button__add">Создать нового мастера</button>
                </div>
            </div>
            <div id="adminContainerForm">
                <AddMasters onAdd={this.addUser} />
            </div>  
        </div>   
        )   
    }

    addUser(user){
        const id = this.state.user.length + 1
        this.setState({user: [...this.state.user, {id, ...user}]})
    }
}


// const app = ReactDOMClient.createRoot(document.getElementById("adminRecordReact"));

// app.render(<adminRecord />);


const domContainer = document.getElementById("adminRecordReact");
ReactDOM.render(e(adminRecord), domContainer);