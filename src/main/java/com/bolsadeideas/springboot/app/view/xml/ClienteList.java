package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="clientes")
public class ClienteList {

	@XmlElement(name="cliente")
	public List<Cliente> clientes;

	public ClienteList() {
	}

	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

}
