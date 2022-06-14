#ifndef VECTOR_H
#define VECTOR_H

#include <cstddef>
#include <stdexcept>
using std::size_t;

// mainVector.cpp header file abstract class Vector.
// All rights Reserved

class Vector {
    enum {CHUNK = 10};
    int* data_ptr;      // Pointer to the heap array
    size_t capacity;    // Size of the current array allocation (total number of ints, in use or not)
    size_t n_elems;     // Number of int spaces currently in use, starting from position 0
    void grow();
public:
    // Object Mgt.
    Vector();
    Vector(const Vector& v);            // Copy constructor
    Vector& operator=(const Vector& v); // Copy assignment operator
    ~Vector();

    // Accessors
    int front() const;                  // Return the int in position 0, if any
    int back() const;                   // Return last element (position n_elems-1)
    int at(size_t pos) const;           // Return element in position "pos" (0-based)
    size_t size() const;                // Return n_elems
    bool empty() const;                 // Return n_elems == 0

    // Mutators
    int& operator[](size_t pos);        // Same as at but no bounds checking
    void push_back(int item);           // Append a new element at the end of the array
    void pop_back();                    // --n_elems (nothing else to do; returns nothing)
    void erase(size_t pos);             // Remove item in position pos and shifts following items left
    void insert(size_t pos, int item);  // Shuffle items right to make room for a new element
    void clear();                       // n_elems = 0 (nothing else to do; keep the current capacity)

    // Iterators
    int* begin();                       // Return a pointer to 1st element, or nullptr if n_elems == 0
    int* end();                         // Return a pointer to 1 past last element, or nullptr if n_elems == 0

    // Comparators
    bool operator==(const Vector& v) const;
    bool operator!=(const Vector& v) const;
};

// Object Mgt.
void Vector::grow(){
    capacity *= 1.6;                    // Increase Vector size by 60%
}

Vector::Vector(){
    data_ptr = new int[CHUNK];          // New Dynamic Storage Pointer
    capacity = CHUNK;                   // Dynamic Memory Sizing
    n_elems = 0;
}

Vector::Vector(const Vector& v){        // Copy constructor from another Vector
    this->data_ptr = v.data_ptr;
    this->capacity = v.capacity;
    this->n_elems = v.n_elems;
}

Vector& Vector::operator=(const Vector& v){  // Copy Assignment aonstructor from another Vector
    for (int i = 0; i < v.n_elems; i++){
        this->push_back(v.at(i));
    }
    return *this;
}

Vector::~Vector(){                      // Vector Deconstructor
    data_ptr = nullptr; 
}

// Accessors
int Vector::front() const{              // Return the int in position 0, if any
    if(n_elems > 0){                    // If size <= 0 throw range error
        return *data_ptr;
    } else {
        throw std::range_error("There are no elements in Vector");
    }
}

int Vector::back() const{               // Return last element (position n_elems-1)
    if(n_elems > 0){                    // If size <= 0 throw range error
        return *(data_ptr + n_elems - 1);
    } else {
        throw std::range_error("There are no elements in Vector");
    }
}

int Vector::at(size_t pos) const{       // Return element in position "pos" (0-based)
    if(n_elems > pos && pos >= 0){      // If pos > n_elems, throw range error
        return *(data_ptr + pos);
    } else {
        throw std::range_error("That is an invalid position");
    }
}

size_t Vector::size() const{            // Return n_elems
    return n_elems;
}

bool Vector::empty() const{             // Return n_elems == 0
    return n_elems == 0;
}

// Mutators
int& Vector::operator[](size_t pos){    // Same as at but no bounds checking
    return *(data_ptr + pos);
}

void Vector::push_back(int item){       // Append a new element at the end of the array
    n_elems += 1;                       // If capacity is full, increase dynamic size
    if(capacity < n_elems){
        grow();
    }
    *(data_ptr + n_elems) = item;
}

void Vector::pop_back(){                // --n_elems (nothing else to do; returns nothing)
    if(n_elems > 0){                    // Throws range error is Vector is empty
        n_elems -= 1;
    } else {
        throw std::range_error("There are no elements in Vector");
    }
}

void Vector::erase(size_t pos){         // Remove item in position pos and shifts following items left
    if(n_elems > 0 && pos >= 0){        // Throws range error is out of range or is Vector is empty
        n_elems -= 1;
        for (int i = pos; i < n_elems; i++){
            *(data_ptr + i) = *(data_ptr + i + 1);
        }
    } else {
        throw std::range_error("There are no elements in Vector");
    }
}
void Vector::insert(size_t pos, int item){  // Shuffle items right to make room for a new element as pos
    if(n_elems == capacity){            // If capacity is full, increase dynamic size
        grow();
    }
    for (int i = n_elems; i > pos; i--){
        *(data_ptr + i) = *(data_ptr + (i - 1));
    }
    *(data_ptr + pos) = item;
    n_elems += 1;
}

void Vector::clear(){                   // n_elems = 0 (nothing else to do; keep the current capacity)
    n_elems = 0;
}

// Iterators
int* Vector::begin(){                   // Return a pointer to 1st element, or nullptr if n_elems == 0
    if (n_elems == 0){
        return nullptr;
    }
    return data_ptr;
}

int* Vector::end(){                     // Return a pointer to 1 past last element, or nullptr if n_elems == 0
    if (n_elems == 0){
        return nullptr;
    }
    return data_ptr + n_elems;
}

// Comparators
bool Vector::operator==(const Vector& v) const{  // Compares every int in lVector and rVector for equality
    if(n_elems != v.n_elems){
        return false;
    } else {
        for(int i = 0; i < n_elems; i++){
            if(*(data_ptr + i) != *(v.data_ptr + i)) { return false; }
        }
        return true;
    }
}
    
bool Vector::operator!=(const Vector& v) const{  // Compares every int in lVector and rVector for inequality
    return !(*this == v);
}

#endif