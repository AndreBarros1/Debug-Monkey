import logo from './logo.svg';
import './App.css';
import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';

function App() {
  const [mensagem, setMensagem] = useState('');
  const [recMensagem, setRecMensagem] = useState([]);
  const textRef = useRef(null);
  const [selectedCli, setSelectedCli] = useState([]); 
  const options = ['fila1_prof', 'fila2_prof'];
  const clienteRef = useRef(selectedCli);

  useEffect(() => {
    clienteRef.current = selectedCli;
    }, [selectedCli]);

  
    const handleChangeCli = async (event) => {

      setSelectedCli(event.target.value);
    };

    const getValorCliente = () => {
      return clienteRef.current;
    }


  // Envia mensagem para o Backend com RabbitMT
  const sendMensagem = () => {
    if (mensagem.trim() === '') return;
    axios({
      method: "post",
      url: 'http://localhost:8099/chatmsg/enviamsg',
      data: mensagem,
      headers: {
          'Content-Type': 'text/plain' // Set content type to plain text
      }})
      .then(() => {
        console.log('Messagem enviada...:', mensagem);
        setMensagem('');
      })
      .catch((err) => console.error('Erro ao enviar mensagem...:', err));
  };

  // Receive messages from RabbitMQ
  useEffect(() => {
    const interval = setInterval(() => {

      const valorCliente = getValorCliente();
      //alert("Valor cliente: " + valorCliente);

      axios({
        method: "get",
        url: 'http://localhost:8099/chatmsg/recebemsg/' + valorCliente,
      })
        .then((response) => {
          if (response.data) {
            console.log("Mensagem recebida...: " + response.data);
            //setRecMensagem(response.data);
            const dados = response.data;
            if(! dados.includes(".")) {
              const msgFormatada = dados.map(msg => msg).join('\n');
              setRecMensagem((prevMessages) => [...prevMessages, msgFormatada]);
              var texmsg = document.getElementById("tex-msg");
              texmsg.scrollTop = this.scrollHeight;
              scrollToBottom();
            }
          }
        })
        .catch((err) => console.error('Erro ao receber menssagens...:', err));
    }, 5000);

    return () => clearInterval(interval);
  }, []);
  
  const limpaMensagens = () => {
    setRecMensagem([]);
  };
  const scrollToBottom = () => {
    if (textRef.current) {
        textRef.current.scrollTop = textRef.current.scrollHeight;
    }
  };
  useEffect(() => {
    scrollToBottom();
  }, [recMensagem]);

  return (

    <div className="App">
     <div> 
      <h1><img src={logo} title='Com REACT!' class="App-logo" />Chat com RabbitMQ Message no Backend</h1>
      <div>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <h3  style={{marginLeft: 40 + '%', padding: 4 + 'px'}}>Escolha um usuário:</h3>
          <select value={selectedCli} onChange={handleChangeCli} class="button">
            <option value="" disabled>Usuários:</option>
              {options.map((option, index) => (
                <option key={index} value={option}>
                 {option}
            </option>
          ))}
          </select>
            {selectedCli && (
        <div>
          <h4 style={{padding: 5 + 'px'}}>{selectedCli}</h4>
        </div>
      )}
    </div>
      </div>
      <input
        type="text"
        value={mensagem}
        onChange={(e) => setMensagem(e.target.value)}
        placeholder="Digite a mensagem a ser enviada..."
      />
      <div></div>
      <button onClick={sendMensagem} class="button">Envia Mensagem</button>
      
      <h2>Messagens Recebidas</h2>
      
      <textarea
        value={recMensagem.join('\n')}    
        readOnly
        rows="15"
        cols="70"
        id="text-msg"
        ref={textRef}
      />
    </div>
      <button onClick={limpaMensagens}>Limpa mensagens...</button>          
    </div>

  );
}

export default App;