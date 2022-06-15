# Definition for singly-linked list.
# The Solution Technique used in this solution is to split the list in half,
# Then reverse the second half of the list and make sure it is equal to the first
# half of the list
#
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution:

    # Function used to revese any LinkedList
    # ex:
    # input: (head) 4 -> 3 -> 2 -> 1 -> None
    # output: (head) 1 -> 2 -> 3 -> 4 -> None
    def reverseNode(self, head) -> ListNode:
        prevNode = None
        while (head != None):
            nextNode = head.next
            head.next = prevNode
            prevNode = head
            head = nextNode

        return prevNode

    def isPalindrome(self, head: Optional[ListNode]) -> bool:
        slow = head
        fast = head

        # Because the fast node is moving 2x the speed as the slow node,
        # by time it gets to the end of the list,
        # slow node will be at the exact middle point of the array
        while fast != None and fast.next != None:
            fast = fast.next.next
            slow = slow.next

        slow = self.reverseNode(slow)
        fast = head

        while slow != None:
            # print(str(fast.val) + ": vs : " + str(slow.val))
            if fast.val != slow.val:
                return False
            fast = fast.next
            slow = slow.next

        return True
