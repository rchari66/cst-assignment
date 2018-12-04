package main

import (
	"net/http"
)

func sayHello(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte(" Hello World!"))
}

func search(w http.ResponseWriter, r *http.Request) {
	query, ok := r.URL.Query()["q"]

	if !ok {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	w.Header().Set("Content-Type", "application/json")

	if query[0] != "" {
		w.Write([]byte("[{\"issueKey\":\"TEST-1\",\"fields\":{\"storyPoints\":10}},{\"issueKey\":\"TEST-2\",\"fields\":{\"storyPoints\":15}}]"))
		// w.Write([]byte(""))
	}
}

func main() {
	http.HandleFunc("/", sayHello)
	http.HandleFunc("/rest/api/2/search", search)
	if err := http.ListenAndServe(":4553", nil); err != nil {
		panic(err)
	}
}
