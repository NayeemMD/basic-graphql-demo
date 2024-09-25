```graphql
mutation {
  addBook(title: "A Song of Ice and Fire Novel series", author: "George R. R. Martin") {
    uuid
    title
    author
    __typename
  }
}
```
```graphql
mutation {
  addReview(bookUuid: "8fc5bed4-46b9-475e-9ac2-8610bb145e11", stars: 5, txt: "Nice") {
    id
    uuid
    stars
    txt
  }
}

```
```graphql
mutation {
updateInventory(bookUuid: "8fc5bed4-46b9-475e-9ac2-8610bb145e11", qty: 10) {
  uuid
  qty
}
}

```
 ```shell
npm install graphql subscriptions-transport-ws
```




```graphql
query {
  books {
    title
    author
    uuid
  }
}

```

```graphql
query {
  book(uuid: "8fc5bed4-46b9-475e-9ac2-8610bb145e11") {
    uuid
    title
  }
}

```

```graphql
query {
  books {
    uuid
    author
    title
    inventory {
      qty
    }
    reviews {
      txt
      stars
    }
  }
}

```