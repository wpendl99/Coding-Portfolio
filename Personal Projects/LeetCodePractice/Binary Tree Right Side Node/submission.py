from collections import deque

# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right


class Solution:
    # What this solution does is it loops through every level of the tree and adds thee right most node to the response.
    # The problem is asking for the most right node on every level of tree NOT just the right side of the tree
    def rightSideView(self, root: Optional[TreeNode]) -> List[int]:
        res = []
        queue = deque()

        if root is not None:
            queue.append(root)

        while len(queue):
            res.append(q[-1].val)
            for i in range(len(queue)):
                nextNode = queue.popleft()
                if nextNode.left:
                    queue.append(nextNode.left)
                if nextNode.right:
                    queue.append(nextNode.right)

        return res
